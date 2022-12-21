# import Tkinter for file selection
import tkinter as tk
from tkinter.filedialog import askopenfilename

# import deepcopy
from copy import deepcopy

# increase Recursion Limit, because of the size of hexmax5.txt
from sys import setrecursionlimit
setrecursionlimit(1010)

# Setting for step output
print_steps_var = False

# Create default Digit Dict for each possible digit
digit_dict = {
    '0' : (True, True, True, True, True, True, False),      # 0
    '1' : (False, True, True, False, False, False, False),  # 1
    '2' : (True, True, False, True, True, False, True),     # 2
    '3' : (True, True, True, True, False, False, True),     # 3
    '4' : (False, True, True, False, False, True, True),    # 4
    '5' : (True, False, True, True, False, True, True),     # 5
    '6' : (True, False, True, True, True, True, True),      # 6
    '7' : (True, True, True, False, False, False, False),   # 7
    '8' : (True, True, True, True, True, True, True),       # 8
    '9' : (True, True, True, True, False, True, True),      # 9
    'A' : (True, True, True, False, True, True, True),      # 10 = A
    'B' : (False, False, True, True, True, True, True),     # 11 = b
    'C' : (True, False, False, True, True, True, False),    # 12 = C
    'D' : (False, True, True, True, True, False, True),     # 13 = d
    'E' : (True, False, False, True, True, True, True),     # 14 = E
    'F' : (True, False, False, False, True, True, True)     # 15 = F
}
# reverse Digit Dict
reversed_digit_dict = {v: k for k, v in digit_dict.items()}

def print_and_write(message, filename):
    """ Function that prints result to console and output file
    p1 -> message -- message to be output
    p2 -> filename -- name of output file

    r  -> None
    """
    with open(filename, 'a') as f:
        print(message, file=f)
    print(message)
def calculate_steps(start_digits, final_digits):
    """ Function that calculates the necessary steps to reach the final digits
    p1 -> start_digits -- original digits before change
    p2 -> final_digits -- final digits after change

    r  -> list of steps

    """
    dif = [[],[]]
    steps = []
    step = deepcopy(start_digits)
    steps.append(deepcopy(start_digits))

    for i,(x1,x2) in enumerate(zip(start_digits, final_digits)):
        for j,(y1,y2) in enumerate(zip(x1, x2)):
            if y1 and not y2:
                dif[0].append((i,j))
            if not y1 and y2:
                dif[1].append((i,j))

    for x1,x2 in zip(dif[0],dif[1]):
        step[x1[0]][x1[1]], step[x2[0]][x2[1]] = step[x2[0]][x2[1]], step[x1[0]][x1[1]]
        steps.append(deepcopy(step))
    return steps
def print_steps(steps, filename):
    """
    p1 -> steps -- list of steps
    p2 -> filename -- name of output file
    
    r  -> None
    """
    for step in steps:
        # create empty lines list
        lines = ['' for _ in range(7)]
        # append each digit to lines
        for digit in step:
            for i,x in enumerate(reversed(digit)):
                # reverse i
                j = 6 - i
                match j:
                    case 6:
                        lines[3] += ' ### ' if x else '     '
                    case 5:
                        lines[1] += '#   ' if x else '    '
                        lines[2] += '#   ' if x else '    '
                    case 4:
                        lines[4] += '#   ' if x else '    '
                        lines[5] += '#   ' if x else '    '
                    case 3:
                        lines[6] += ' ### ' if x else '     '
                    case 2:
                        lines[4] += '#' if x else ' '
                        lines[5] += '#' if x else ' '
                    case 1:
                        lines[1] += '#' if x else ' '
                        lines[2] += '#' if x else ' '
                    case 0:
                        lines[0] += ' ### ' if x else '     '
            # create space between digits
            for i,_ in enumerate(lines):
                lines[i] += '  '
        # print lines
        for x in lines:
            print_and_write(x, filename)
        print_and_write('\n', filename)
def read_file():
    """ Function that reads the input file.
    r1 -> digits -- list of input digits
    r2 -> n_of_changes -- max number of changes

    """
    def choose_file():
        """ Function that creates a window to choose a file
        r  -> filename -- filename of choosen file

        """
        root = tk.Tk()
        root.withdraw()
        root.overrideredirect(True)
        root.geometry('0x0+0+0')
        root.deiconify()
        root.lift()
        root.focus_force()
        filename = askopenfilename(parent=root, initialdir='beispieldaten/')
        root.destroy()
        return filename
    filename = choose_file()
    with open(filename) as f:
        digits = (x for x in f.readline().strip())
        n_of_changes = int(f.readline().strip())
    return digits, n_of_changes, filename
def convert_digits(intDigits):
    """ Function that converts digits to 7-Segment format
    p1 -> intDigits -- tuple of digits

    r  -> digits -- list of digits in 7-Segment format

    """
    return [list(digit_dict[x]) for x in intDigits]
def calculate_final_digits(digits, max_n_of_changes):
    """ Function that calculates the final digits after n-Changes
    p1 -> digits -- list of digits in
    p2 -> max_n_of_changes -- maximum number of line-changes

    r1 -> best_digits -- best digits after line-changes
    r2 -> best_left_n_of_changes -- left line-changes after line-changes

    """
    def calculate_dismatchings(d1,d2):
        """ This function calculates the number of dismachings between two digits and the difference between the number of lines
        p1 -> d1 -- digit 1
        p2 -> d2 -- digit 2
        
        r1 -> number of machings (number of required changes)
        r2 -> difference between number of lines

        """
        n = [0,0]
        for x1,x2 in zip(d1,d2):
            if x1 and not x2:
                n[0]+=1
            elif not x1 and x2:
                n[1]+=1
        return max(n), n[0]-n[1]
    def check_lines(digit_index, current_digits, lines_cache):
        """ This function checks if interim result is stil possible
        p1 -> digit_index -- current index of digit
        p2 -> current_digits -- current interim digits 
        p3 -> lines_cache -- interim cache of lines

        r  -> possibleness of the result

        """
        _sum = 0
        # calculate sum of lines (start at current index)
        for digit in current_digits[digit_index:]:
            _sum += sum(digit)
        if lines_cache > 0:
            # return if there is enough space for all lines that are in lines_cache
            return len(current_digits[digit_index:])*7-_sum >= lines_cache
        elif lines_cache < 0:
            # return if there are enough lines to balance lines_cache
            return _sum >= abs(lines_cache)
        return True
    def calculate_next_digit(digit_index, current_digits, lines_cache, left_n_of_changes):
        """ Function that works recursive and calculates the next digit
        p1 -> digit_index -- current index of digit
        p2 -> current_digits- current interim digits 
        p3 -> lines_cache -- interim cache of lines
        p4 -> left_n_of_changes -- left number of changes

        r  -> None
        
        """
        # reference on global variables
        global finished
        global best_digits
        global best_left_n_of_changes

        # check if interim solution is possible and the algorithm isn't finished
        if not finished and check_lines(digit_index, current_digits, lines_cache):
            cur_digit = current_digits[digit_index]
            # loop through digit dict reversed
            for new_digit in reversed(digit_dict.values()):
                lines_cache_copy = lines_cache
                # calculate matchings and difference between line numbers
                n_this_change, this_lines_cache = calculate_dismatchings(cur_digit, new_digit)
                # use lines cache to minimize changes
                while lines_cache_copy > 0 and this_lines_cache < 0:
                    this_lines_cache += 1
                    n_this_change -= 1
                    lines_cache_copy -= 1
                while lines_cache_copy < 0 and this_lines_cache > 0:
                    this_lines_cache -= 1
                    n_this_change -= 1
                    lines_cache_copy += 1
                # check if enough changes are left
                if n_this_change <= left_n_of_changes:
                    lines_cache_copy+=this_lines_cache
                    new_current_digits = current_digits
                    new_current_digits[digit_index] = list(new_digit)
                    # if this isn't the last digit calculate next
                    if digit_index+1 < len(current_digits):
                        # recursive call of this function
                        calculate_next_digit(digit_index+1, new_current_digits, lines_cache_copy, left_n_of_changes-n_this_change)
                    else:
                        # Check if solution is valid
                        if lines_cache_copy == 0:
                            # Save solution and finish the algorithm
                            finished = True
                            best_digits = new_current_digits.copy()
                            best_left_n_of_changes = left_n_of_changes-n_this_change
                            break
    global finished
    finished = False
    # start recursive function
    calculate_next_digit(0, digits.copy(), 0, max_n_of_changes)
    return best_digits, best_left_n_of_changes
def main():
    """  Main Function that is executed at the beginning"""

    # get file
    digits, max_n_of_changes,filename = read_file()
    output_filename = 'beispielausgaben/{}.out'.format(filename.split('/')[-1].split('.')[0])

    # convert digits to 7-Segment format
    digits = convert_digits(digits)
    
    # Calculate digits
    final_digits,left_n_of_changes = calculate_final_digits(digits, max_n_of_changes)

    # print out steps
    if print_steps_var:
        steps = calculate_steps(digits, final_digits)
        step_output_filename = output_filename.split('.')[0]+'_steps.'+output_filename.split('.')[1]
        open(step_output_filename,'w').close()
        print_steps(steps, step_output_filename)

    # prinout results
    open(output_filename,'w').close()
    print_and_write('Before  -->  '+''.join(str(y)+' ' for y in [reversed_digit_dict[tuple(x)] for x in digits]), output_filename)
    print_and_write('After   -->  '+''.join(str(y)+' ' for y in [reversed_digit_dict[tuple(x)] for x in final_digits]), output_filename)
    print_and_write('Left Changes  -->  '+str(left_n_of_changes), output_filename)

# only execute if script is direct executed
if __name__ == '__main__':
    main()